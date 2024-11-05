from rest_framework import serializers
from .models import Carrito, DetallePedido, Pedido

class CarritoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Carrito
        fields = ['id', 'producto', 'cantidad', 'usuario']

class DetallePedidoSerializer(serializers.ModelSerializer):
    class Meta:
        model = DetallePedido
        fields = ["cantidad_productos", "precio_producto", "subtotal"]

class ModificarCantidadSerializer(serializers.Serializer):
    cantidad = serializers.IntegerField(min_value=1)        

class CarritoResponseSerializer(serializers.Serializer):
    id = serializers.IntegerField()
    producto = serializers.CharField()
    cantidad = serializers.IntegerField(min_value=1)
    precio = serializers.DecimalField(max_digits=10, decimal_places=2)
    imageURL = serializers.URLField()

class DashboardDetailSerializer(serializers.Serializer):
    fecha_pedido = serializers.DateField()
    direccion_entrega = serializers.CharField()
    estado = serializers.CharField()
    detalles = DetallePedidoSerializer(many=True)

class ApiResponseSerializer(serializers.Serializer):
    message = serializers.CharField(required=False)
    error = serializers.CharField(required=False)
