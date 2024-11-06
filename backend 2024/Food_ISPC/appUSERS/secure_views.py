from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated
from rest_framework_simplejwt.tokens import RefreshToken
from .serializers import (
    AuthTokenSerializer,
    TokenResponseSerializer, 
    MessageResponseSerializer,
    UsuarioSerializer
)
import logging

# Configure logging
logger = logging.getLogger(__name__)

class SecureCreateTokenView(APIView):
    serializer_class = AuthTokenSerializer

    def post(self, request, *args, **kwargs):
        try:
            # Validate input
            serializer = self.serializer_class(data=request.data, context={'request': request})
            serializer.is_valid(raise_exception=True)
            user = serializer.validated_data['user']
            
            refresh = RefreshToken.for_user(user)
            access_token = refresh.access_token

            response_data = {
                'email': user.email,
                'user_id': user.pk, 
                'refresh': str(refresh),
                'access': str(access_token),
                'nombre': user.nombre, 
                'apellido': user.apellido,
                'telefono': user.telefono,
                'admin': user.is_superuser
            }

            # Validate response data
            response_serializer = TokenResponseSerializer(data=response_data)
            if not response_serializer.is_valid():
                logger.error(f"Invalid token response format: {response_serializer.errors}")
                return Response(
                    {'error': 'Invalid response format'}, 
                    status=status.HTTP_500_INTERNAL_SERVER_ERROR
                )

            return Response(response_serializer.validated_data, status=status.HTTP_200_OK)
        except Exception as e:
            logger.error(f"Error in SecureCreateTokenView: {str(e)}")
            return Response(
                {'error': 'An unexpected error occurred'}, 
                status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )

class SecureCreateUserView(APIView):
    def post(self, request):
        try:
            # Validate input
            serializer = UsuarioSerializer(data=request.data)
            serializer.is_valid(raise_exception=True)
            
            # Create user
            user = serializer.save()
            
            response_data = {
                'detalle': 'Usuario creado exitosamente'
            }
            
            # Validate response
            response_serializer = MessageResponseSerializer(data=response_data)
            if not response_serializer.is_valid():
                logger.error(f"Invalid create user response format: {response_serializer.errors}")
                return Response(
                    {'error': 'Invalid response format'}, 
                    status=status.HTTP_500_INTERNAL_SERVER_ERROR
                )
                
            return Response(response_serializer.validated_data, status=status.HTTP_201_CREATED)
        except Exception as e:
            logger.error(f"Error in SecureCreateUserView: {str(e)}")
            return Response(
                {'error': 'An unexpected error occurred'}, 
                status=status.HTTP_500_INTERNAL_SERVER_ERROR
            ) 