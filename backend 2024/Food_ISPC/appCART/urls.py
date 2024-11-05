from django.urls import path
from .views import *
#from .secure_views import SecureVerCarrito, SecureVerDashboard

urlpatterns = [
    path('agregar/<int:producto_id>/', AgregarProductoAlCarrito.as_view()),
    path('ver/', VerCarrito.as_view()),
    path('confirmar/', ConfirmarPedido.as_view()),
    path('eliminar/<int:carrito_id>/', EliminarProductoDelCarrito.as_view()),
    path('ver_dashboard/', VerDashboard.as_view()),
    path('modificar_cantidad/<int:carrito_id>/', ModificarCantidadProductoCarrito.as_view(), name='modificar_cantidad_producto_carrito'),
    #path('carrito/', SecureVerCarrito.as_view(), name='ver_carrito'),
    #path('dashboard/', SecureVerDashboard.as_view(), name='ver_dashboard'),
]

