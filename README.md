# java-explore-with-me
[Ссылка на пул реквест](https://github.com/Bervy/java-explore-with-me/pull/2)

В рамках дипломного проекта разработано REST API для приложения ExploreWithMe (англ. «исследуй со мной»). Оно предоставляет возможность делиться информацией об интересных событиях и помогать найти компанию для участия в них.
### _Приложение включает в себя сервисы:_

- GateWay (разрабатывается отдельно)
    - Проверяет права пользователей
    - Передает запросы на остальные микросервисы в зависимости прав
- Основной сервис — содержит всё необходимое для работы.
    - Просмотр событий без авторизации;
    - Возможность создания и управления категориями;
    - События и работа с ними - создание, модерация;
    - Запросы пользователей на участие в событии - запрос, подтверждение, отклонение.
    - Создание и управление подборками.
    - Добавление и удаление Лайков событиям, формирование рейтингов.
- Сервис статистики — хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.
    - Отдельный сервис для сбора статистики;

### _Описание сервисов:_

#### _Основной сервис, выделенный порт: 8080_

- **Публичный** (_доступен для всех пользователей_)
    - API для работы с событиями
    - API для работы с категориями
- **Приватный** (_доступен только для зарегистрированных пользователей_)
    - API для работы с событиями
    - API для работы с запросами текущего пользователя на участие в событиях
    - API для работы с рейтингами
- **Административный** (_доступен только для администратора проекта_)
    - API для работы с событиями
    - API для работы с категориями
    - API для работы с пользователями
    - API для работы с подборками событий

#### _Сервис статистики, выделенный порт: 9090_

- **Административный** (_доступен только для администратора проекта_)
    - API для работы со статистикой посещений