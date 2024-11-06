from django.urls import path
from appUSERS import views
#from .secure_views import SecureCreateTokenView, SecureCreateUserView


urlpatterns = [
    path('register/', views.CreateUsuarioView.as_view()),
    path('login/', views.CreateTokenView.as_view()),
    path('logout/', views.LogoutView.as_view()),
    path('update/', views.UpdateProfileView.as_view(), name='update-profile'),
    path('delete/', views.DeleteProfileView.as_view(), name='delete-profile'),
    #path('token/', SecureCreateTokenView.as_view(), name='token'),
    #path('create/', SecureCreateUserView.as_view(), name='create'),
]

 