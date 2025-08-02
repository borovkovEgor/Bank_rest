# Система управления банковскими картами

### 1. Запуск с Docker Compose
```bash
docker compose up -d
```

### Автоматическая инициализация
При первом запуске Liquibase автоматически создаст:
- Таблицы: `users`, `cards`, `transactions`
- Индексы для оптимизации запросов
- Тестовые данные

### Тестовые пользователи
```
ADMIN:
- Username: admin
- Password: admin123
- Role: ADMIN

USER1:
- Username: user1
- Password: user123
- Role: USER

USER2:
- Username: user2
- Password: user123
- Role: USER
```

После запуска приложения документация доступна по адресу:
- **Swagger UI**: http://localhost:8080/swagger-ui.html

