from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated
from .models import Carrito, DetallePedido, Pedido
from .serializers import (
    CarritoResponseSerializer, 
    DashboardDetailSerializer, 
    ApiResponseSerializer,
    DetallePedidoSerializer
)
import logging

# Configure logging
logger = logging.getLogger(__name__)

class SecureVerCarrito(APIView):
    permission_classes = [IsAuthenticated]
    
    def get(self, request):
        try:
            usuario = request.user
            id_usuario = usuario.id_usuario

            detalles_carrito = Carrito.objects.select_related("id_pedido").all().filter(usuario_id=id_usuario)
            carrito_data = [
                {
                    "id": detalle.id,
                    'producto': detalle.producto.nombre_producto,
                    'cantidad': detalle.cantidad,
                    "precio": detalle.producto.precio,
                    "imageURL": detalle.producto.imageURL
                } for detalle in detalles_carrito
            ]

            # Validate response data
            serializer = CarritoResponseSerializer(data=carrito_data, many=True)
            if not serializer.is_valid():
                logger.error(f"Invalid cart response format: {serializer.errors}")
                return Response(
                    {'error': 'Invalid response format'}, 
                    status=status.HTTP_500_INTERNAL_SERVER_ERROR
                )
            
            return Response(serializer.validated_data)
        except Exception as e:
            logger.error(f"Error in SecureVerCarrito: {str(e)}")
            return Response(
                {'error': 'An unexpected error occurred'}, 
                status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )

class SecureVerDashboard(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        try:
            usuario = request.user
            id_usuario = usuario.id_usuario
            vistaPedidos = Pedido.objects.prefetch_related('detalles').all().filter(id_usuario_id=id_usuario)

            dashboard_data = [
                {
                    "fecha_pedido": pedido.fecha_pedido,
                    "direccion_entrega": pedido.direccion_entrega,
                    "estado": pedido.estado,
                    "detalles": DetallePedidoSerializer(pedido.detalles.all(), many=True).data
                } for pedido in vistaPedidos
            ]

            # Validate response data
            serializer = DashboardDetailSerializer(data=dashboard_data, many=True)
            if not serializer.is_valid():
                logger.error(f"Invalid dashboard response format: {serializer.errors}")
                return Response(
                    {'error': 'Invalid response format'}, 
                    status=status.HTTP_500_INTERNAL_SERVER_ERROR
                )

            return Response({"results": serializer.validated_data})
        except Exception as e:
            logger.error(f"Error in SecureVerDashboard: {str(e)}")
            return Response(
                {'error': 'An unexpected error occurred'}, 
                status=status.HTTP_500_INTERNAL_SERVER_ERROR
            ) 