### Взаимосвязь старого API и функционала аутентификации

1. **Аутентификация через JWT**:
    - В новой версии системы добавлен контроллер `AuthController`, который обрабатывает запросы `/register`, `/login`, и `/reset-password`.
    - После успешного логина, пользователю возвращается JWT токен, который необходимо использовать в заголовке `Authorization` при обращении к защищенным API.
    - Пример заголовка: `Authorization: Bearer <token>`.

2. **Защита эндпоинтов**:
    - Использование Spring Security позволяет ограничить доступ к API с помощью JWT токена.
    - В `SecurityConfig` метод `securityFilterChain` определяет правила доступа:
      ```java
      http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
      ```
    - Это значит, что запросы к `/api/auth/**` (регистрация, логин, сброс пароля) доступны без аутентификации. Все остальные запросы требуют JWT токен.

3. **Интеграция старого сервиса**:
    - Старые контроллеры, такие как `category-controller`, `location-controller`, и `event-controller`, теперь защищены аутентификацией.
    - Если пользователь пытается вызвать методы, например, `GET /api/v1/places/categories` или `POST /api/v1/locations`, без JWT токена в заголовке, система вернет ошибку 401 Unauthorized.

4. **Использование фильтра JWT**:
    - В конфигурации безопасности используется фильтр `JwtAuthenticationFilter`, который перехватывает все запросы и проверяет наличие токена.
    - Если токен валиден, пользовательский контекст (данные о пользователе) добавляется в SecurityContext, и запрос проходит дальше.
    - Если токен отсутствует или недействителен, запрос блокируется.

### Последовательность действий пользователя:
1. Пользователь регистрируется через `/api/auth/register`.
2. Затем проходит аутентификацию через `/api/auth/login` и получает JWT токен.
3. Использует этот токен для доступа к другим API, таким как:
    - `/api/v1/places/categories` — API с категориями мест.
    - `/currencies/convert` — API конвертации валют.
    - `/api/v1/events` — API с событиями.

Таким образом, новый функционал аутентификации теперь является обязательным шагом перед использованием всех основных API в системе.
