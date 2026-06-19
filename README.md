# Recipe App

Android-приложение для поиска и просмотра рецептов с каталогом категорий, избранными рецептами и пошаговыми инструкциями приготовления.

<p align="center">
  <img src="https://github.com/user-attachments/assets/7708b072-6065-450f-a579-f30d3c74755a" width="20%"/> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/9be7fe2d-2c23-431c-8a09-7b262551d7a2" width="20%"/> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/51b13db6-291c-43ff-a68e-67a31d597b4c" width="20%"/>
</p>

## О проекте

Приложение позволяет просматривать рецепты различных категорий, получать подробную информацию о блюдах, сохранять понравившиеся рецепты в избранное и изменять количество порций с автоматическим пересчётом ингредиентов.

## Основные возможности

- Просмотр категорий блюд
- Каталог рецептов по категориям
- Детальная информация о рецепте
- Пошаговые инструкции приготовления
- Список ингредиентов
- Избранные рецепты
- Автоматический пересчёт ингредиентов под выбранное количество порций
- Работа без интернета для сохранённых данных
- Наличие темной темы приложения
- Возможность делиться рецептами

## Технологии

- UI: Jetpack Compose + Material Design 3  
- State Management: StateFlow  
- Architecture: MVVM + Repository Pattern  
- DI: Hilt(Dagger)
- Network: Retrofit2 + OkHttp3  
- Database: Room  
- Async: Kotlin Coroutines  
- Images: Coil  
- Testing: JUnit + Mockk + Kaspresso

## Установка

1. Клонировать репозиторий

```bash
git clone <repository-url>
```

2. Открыть проект в Android Studio

3. Выполнить синхронизацию Gradle

4. Запустить приложение на устройстве или эмуляторе
