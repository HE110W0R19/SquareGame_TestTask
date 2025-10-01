
# Документация по проекту «Squares Game»

## Цель проекта

Реализация классической игры **«Квадраты»** в формате веб-приложения.

Пользователь играет против компьютера: каждый по очереди ставит фишку своего цвета на поле NxN. Побеждает тот, кто первым составит квадрат из своих фишек (любой ориентации).

Архитектура разделена на 2 части:

* **Backend (Java + Spring Boot)** – движок игры и REST API.
* **Frontend (Next.js + React + TypeScript)** – визуальный интерфейс, который общается с API.

---

## Архитектура

### Backend

* Ядро игры (engine) вынесено в отдельный модуль.
* API реализовано на Spring Boot (чистая архитектура с разделением слоёв: `domain`, `usecase`, `adapters`).
* Эндпоинт:
  * `POST /api/v1/next-move` – принимает состояние доски и возвращает ход компьютера + статус игры.

### Frontend

* **Фреймворк:** Next.js 15 (App Router)
* **Язык:** TypeScript
* **UI:** React с CSS без библиотек стилей (легкий кастом).
* **Паттерн:** функциональные компоненты, hooks (`useState`, `useMemo`).
* **Структура:**
  ```
  .square-game/
   ├── app/page.tsx         # Главная страница
   ├── components/Board.tsx # Игровое поле
   ├── lib/api.ts           # Обёртка для API-запросов
   ├── public/              # Статические файлы
   └── styles/globals.css   # Стили
  ```

---

## Алгоритм работы фронтенда

1. Пользователь выбирает **размер поля** и  **цвет фишек** .
2. При клике на пустую клетку:
   * Фишка игрока ставится локально в массив `board`.
   * Отправляется запрос на бэкенд (`/api/next-move`) с параметрами: `size`, `next` (цвет компьютера), `board`.
3. Бэкенд:
   * Проверяет, выиграл ли игрок.
   * Если нет — вычисляет ход компьютера.
   * Возвращает новый статус:  **Win** ,  **Draw** , или следующий ход.
4. Фронтенд:
   * Отрисовывает ответ (ставит фишку компьютера).
   * Обновляет статус игры.
5. Игра продолжается до победы одного из игроков или ничьей.

---

## Компоненты и методы

### `lib/api.ts`

```ts
export async function requestNextMove(size, next, board)
```

* Делает `fetch` на Spring Boot API.
* Принимает `size` (размер), `next` (цвет игрока, который ходит), `board` (двумерный массив с состоянием).
* Возвращает JSON с:
  * `finished` – закончена ли игра,
  * `winner` – цвет победителя или `null`,
  * `move` – координаты хода компьютера,
  * `message` – текст («Continue», «Win», «Draw»).

---

### `components/Board.tsx`

```tsx
function Board({ size, board, onCellClick, disabled })
```

* Рисует поле NxN (flex-grid через CSS).
* Каждая клетка:
  * пустая → кнопка, нажатие вызывает `onCellClick(x, y)`.
  * белая → ⚪.
  * чёрная → ⚫.
* Проп `disabled` запрещает клики (например, когда игра закончена или сейчас ход компьютера).

---

### `app/page.tsx`

Главный контроллер состояния игры.

**Стейты:**

* `size` – размер поля.
* `userColor` / `aiColor` – цвета игрока и компьютера.
* `board` – текущее состояние (NxN массив).
* `next` – чей следующий ход.
* `status` – текстовое состояние игры.
* `finished` – флаг завершения.
* `busy` – индикатор, что ждём ответ API.

**Методы:**

```tsx
const resetGame = () => { ... }
```

* Пересоздаёт пустое поле с текущим `size`.
* Сбрасывает статус.
* Устанавливает `next = userColor`.

```tsx
const handleCellClick = async (x, y) => { ... }
```

* Ставит фишку пользователя в `board`.
* Отправляет `board` на API.
* Обрабатывает ответ: если конец игры → пишет статус; иначе ставит фишку компьютера.

---

## Использованные паттерны и приёмы

* **Чистая архитектура (Clean Architecture):**
  * Бэкенд разделён на доменные сущности (`Board`, `Move`, `GameState`), usecases (`NextMoveUseCase`), адаптеры (REST-контроллер).
  * Фронт разделён на слои:
    * `lib/api.ts` – инфраструктура (работа с сетью).
    * `components/Board.tsx` – представление.
    * `app/page.tsx` – слой сценариев (game flow).
* **SOLID:**
  * SRP – каждый модуль отвечает за одно: API, UI-отрисовка, управление состоянием.
  * DIP – фронт знает только контракт API (`requestNextMove`), не детали реализации движка.
* **React Hooks:** управление состоянием (`useState`), мемоизация значений (`useMemo`).
* **Функциональный стиль:** компоненты без классов, только функции и props.

---

## Запуск

### Backend (Spring Boot)

```bash
cd SquareGameApi
mvn spring-boot:run
```

По умолчанию поднимется на `http://localhost:8080`.

### Frontend (Next.js)

```bash
cd .square-game
npm install
npm run dev
```

Приложение доступно на [http://localhost:3000](http://localhost:3000/).

---

## Инструкция для пользователя

1. Открыть фронтенд в браузере.
2. Ввести **размер поля** (N ≥ 3).
3. Выбрать **цвет фишек** (белые или чёрные).
4. Нажать  **Новая игра** .
5. Кликать на пустые клетки → ставить свои фишки.
6. Компьютер будет автоматически отвечать своим ходом.
7. Следить за статусом:
   * «Ваш ход»
   * «Ход компьютера»
   * «Вы выиграли!» / «Компьютер выиграл!» / «Ничья».

---

## Возможные улучшения

* Подсветка выигрышного квадрата.
* История ходов.
* Подсветка последнего хода.
* Настройка «кто ходит первым».
* Поддержка разных AI-стратегий (случайный, жадный, минимакс).


# Ниже — подробная «техническая» документация по **backend** (Java/Spring Boot) для сервиса ходов «Квадраты». Я разложил по слоям (clean/onion), описал каждый публичный метод, схемы DTO, логику, алгоритмы и важные нюансы.

---

# Архитектура сервиса (Clean/Onion)

```
com.squaregameapi.task.api
 ├─ SquaresServiceApp             ← @SpringBootApplication (точка входа)
 ├─ domain/
 │   ├─ model/
 │   │   ├─ CellColor             ← enum: W/B/E
 │   │   ├─ BoardState            ← снимок состояния доски (N, next, матрица)
 │   │   ├─ Move                  ← один ход (x,y,color)
 │   │   └─ MoveResponse          ← ответ API (finished/winner/move/message)
 │   └─ port/
 │       └─ MoveCalculator        ← ПОРТ: расчёт следующего хода
 ├─ application/
 │   └─ CalculateNextMoveUseCase  ← USE CASE: фасад над портом
 ├─ adapters/
 │   ├─ in/web/
 │   │   ├─ MoveController        ← REST-эндоинт POST /api/v1/next-move
 │   │   └─ (DTO, если отделяете web-DTO от domain-DTO)
 │   └─ out/engine/
 │       └─ EngineMoveCalculator  ← АДАПТЕР к «движку» из задания 1
 └─ infrastructure/
     ├─ OpenApiConfig             ← опционально: заголовок/описание OpenAPI
     └─ CorsConfig                ← опционально: CORS для фронта (localhost:3000)
```

Связи:

`Controller → UseCase → Port (MoveCalculator) → Adapter(EngineMoveCalculator) → engine(Board, ComputerPlayer, …)`

Web-слой ничего не знает о подробностях движка — только про **порт** (`MoveCalculator`).

---

## Domain Layer

### `enum CellColor { W, B, E }`

* Значения: `W` (белые), `B` (чёрные), `E` (пусто).
* Используется во всех DTO сервиса.

### `@Value @Builder class BoardState`

Снимок входного состояния доски.

Поля:

* `int size` — N, размер поля (N×N). Требования: `N >= 3`.
* `CellColor next` — **кто должен ходить следующим** на этом состоянии.
* `List<List<CellColor>> board` — матрица `size × size`, индексируется как `board[y][x]`.

Назначение:

* Является **единственным источником истины** для расчёта.
* Сервис из него **восстанавливает** внутренний `Board` движка.

### `@Data class Move`

* `int x, y` — координаты хода.
* `CellColor color` — цвет, который ставится в (x,y).

### `@Value @Builder class MoveResponse`

Ответ сервиса.

Поля:

* `boolean finished` — закончена ли игра на  **текущем** /**следующем** состоянии (после хода).
* `CellColor winner` — победитель (`W`/`B`) или `null` (нет победителя).
* `Move move` — ход, предложенный сервисом (обычно ход компьютера), или `null`, если игра уже завершена.
* `String message` — «Continue» | «Win» | «Draw» | «Game already finished» | «No moves».

---

## Port (Domain → Outside)

### `interface MoveCalculator`

```java
MoveResponse calculate(BoardState state);
```

* Контракт расчёта **следующего хода** для переданного состояния доски.
* Не зависит от web/engine — чистая абстракция.
* Все реализации обязаны:
  1. корректно интерпретировать `BoardState`;
  2. возвращать `MoveResponse` с актуальными флагами/победителем.

---

## Application Layer

### `@Service class CalculateNextMoveUseCase`

```java
MoveResponse handle(BoardState state)
```

* Тонкий сервис-оркестратор (use case).
* Делегирует в `MoveCalculator#calculate`, но даёт точку расширения (логирование, метрики, валидация бизнес-правил на уровне приложения, кэширование и т.д.).
* Возвращает  **ровно то** , что вернул порт.

---

## Adapters — Inbound (Web)

### `@RestController class MoveController`

Маршрут: `POST /api/v1/next-move`

Consumes/Produces: `application/json`.

 **Метод** :

```java
MoveResponseDto next(@Validated @RequestBody MoveRequestDto req)
```

Шаги:

1. **Валидация входа** (`@Validated`):
   * `size >= 3`;
   * `board != null` и матрица корректной формы;
   * `next ∈ {W,B}` (обычно `E` для next не используется).
2. **Сборка доменного `BoardState`** из web-DTO (часто в 1:1):
   ```java
   var state = BoardState.builder()
       .size(req.getSize())
       .next(req.getNext())
       .board(req.getBoard())
       .build();
   ```
3. **Вызов use case** : `useCase.handle(state)`.
4. **Сборка web-ответа** (если web DTO не совпадает с доменным):
   * Перекопировать поля `finished/winner/message`;
   * Если есть `move` — вложить `x/y/color`.

**Ошибки/исключения** (по умолчанию Spring Boot):

* Неверные данные запроса (`MethodArgumentNotValidException`) → `400 Bad Request` с телом об ошибке.
* Любые непойманные исключения → `500 Internal Server Error`.
* Можно добавить `@ControllerAdvice` для кастомного формата ошибок (не обязательно по ТЗ).

---

## Adapters — Outbound (Engine)

### `@Component class EngineMoveCalculator implements MoveCalculator`

Ключевой адаптер, который **инкапсулирует вызов движка** из задания 1.

 **Метод** :

```java
@Override
public MoveResponse calculate(BoardState state)
```

Пошагово:

1. **Инициализация внутреннего поля движка** :

```java
   int n = state.getSize();
   Board board = new Board(n);   // из модуля engine (Задание 1)
```

1. **Восстановление клеток** :

```java
   for (int y = 0; y < n; y++) {
     var row = state.getBoard().get(y);
     for (int x = 0; x < n; x++) {
       if (row.get(x) == E) continue;
       board.place(x, y, (row.get(x) == W) ? Color.W : Color.B);
     }
   }
```

   Важно: `place` возвращает `boolean`, но в адаптере состояние считается валидным, и мы не обрабатываем `false` (пустые клетки/границы уже проверены на стороне контроллера/клиента).

1. **Быстрые финальные проверки** на текущем состоянии:
   ```java
   boolean wWin = board.hasSquare(Color.W);
   boolean bWin = board.hasSquare(Color.B);
   if (wWin || bWin) 
       return finished(winner = wWin ? W : B, message = "Game already finished");
   if (board.full())
       return finished(winner = null, message = "Draw");
   ```
2. **Определение, чей ход** :

```java
   Color next = (state.getNext() == W) ? Color.W : Color.B;
```

1. **Генерация хода** (используем ИИ из движка — `ComputerPlayer`):
   ```java
   Player p = new ComputerPlayer(next, board);
   Point m = p.nextMove();  // может вернуть null, если нет ходов
   if (m == null) return finished(null, "No moves");
   ```
2. **Применение хода** :

```java
   board.place(m.x, m.y, next);
   boolean win = board.hasSquare(next);
   boolean full = board.full();
```

1. **Формирование ответа** :

* Если `win` → `finished=true, winner=next, message="Win"`, `move` — (m.x,m.y,next).
* Если `full` → `finished=true, winner=null, message="Draw"`, `move` — (m.x,m.y,next) — он уже сделан.
* Иначе → `finished=false, winner=null, message="Continue"`, `move` — (m.x,m.y,next).

 **Замечания** :

* Мы **не используем** `Game` из движка — сервису нужен один статический расчёт **без** цикла партии.
* Стратегия компьютера в `ComputerPlayer` по ТЗ может быть простой (рандом из пустых). При желании можно подменить реализацию (например, «выигрыш/блок за 1 ход»), **не меняя** порт/контроллер.

---

# Внутренний «движок» (из Задания 1) — ключевые методы

> Эти классы живут в отдельном модуле (engine) и переиспользуются сервисом «как библиотека». Ниже — основа.

### `class Board`

Хранит состояние поля и ускоряет проверку квадратов.

Поля:

* `int n` — размер.
* `Color[][] cells` — содержимое клеток (`null`/W/B).
* `Map<Color, Set<Point>> byColor` — индекс своих фишек для O(1)-поиска.

Методы:

* `Board(int n)` — создаёт пустую доску `n×n`.
* `int size()` — возвращает `n`.
* `boolean in(int x, int y)` — внутри ли координаты.
* `boolean isEmpty(int x, int y)` — свободна ли клетка (и внутри).
* `boolean place(int x, int y, Color c)` — ставит фишку, если можно (внутри и пусто), возвращает `true`/`false`.
* `boolean full()` — занято ли всё поле.
* `List<Point> emptyCells()` — список пустых клеток (для ИИ).
* `boolean hasSquare(Color c)` — **главный алгоритм** проверки квадрата:
  * Берём множество точек `S` цвета `c`.
  * Для каждой пары `A,B ∈ S, A≠B` строим вектор `v = (dx,dy) = B−A`.
  * Поворот на ±90°:
    * `v⊥₁ = (-dy, dx)`, соответствующие точки:

      `C = A + v⊥₁`, `D = B + v⊥₁`
    * `v⊥₂ = (dy, -dx)`, соответствующие точки:

      `C' = A + v⊥₂`, `D' = B + v⊥₂`
  * Если `(C,D) ⊂ S` **и** обе в пределах доски → квадрат найден.
  * Сложность: `O(k²)` по количеству фишек `k` данного цвета; проверка членства — `O(1)` за счёт `HashSet`.

Плюсы метода:

* Находит **любой поворот** квадрата (не только «по клеткам»).
* Корректен на целочисленной решётке: все вершины остаются в целых координатах.

Краевые случаи:

* `k < 4` → сразу `false`.
* Дубликаты и выход за границы отбрасываются `in(x,y)`.

### `enum Color { W, B }`

Цвет для движка (без `E`). Маппится из/в `CellColor`.

### `record/class Point(int x, int y)`

Координаты фишки, с корректными `equals/hashCode`.

### `interface Player { Color color(); boolean isHuman(); Point nextMove(); }`

### `class ComputerPlayer implements Player`

Простая стратегия:

* Берёт `board.emptyCells()`.
* Выбирает случайную клетку.
* Возвращает `Point` или `null`, если ходов нет.

> В проде можно заменить на «поиск победы/блока за ход», мини-симуляции и т.д.,  **не меняя порт** .

---

# OpenAPI/Swagger

### Зависимость

`org.springdoc:springdoc-openapi-starter-webmvc-ui`

### Конфиг (опционально)

```java
@Bean
OpenAPI api() {
  return new OpenAPI().info(new Info()
    .title("Squares Move Service")
    .version("1.0.0")
    .description("Calculate next move and game result for Squares"));
}
```

### URL’ы

* UI: `/swagger-ui.html` → редирект на `/swagger-ui/index.html`
* Спека: `/v3/api-docs` (`.yaml` тоже доступен: `/v3/api-docs.yaml`)

---

# CORS (для фронта на Next.js, :3000)

Вариант через код:

```java
@Configuration
class CorsConfig {
  @Bean WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override public void addCorsMappings(CorsRegistry r) {
        r.addMapping("/**")
         .allowedOrigins("http://localhost:3000")
         .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
         .allowedHeaders("*");
      }
    };
  }
}
```

Или через свойства:

```properties
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```

> Альтернатива — прокси через Next API routes (`/app/api/next-move/route.ts`) и звать с фронта `/api/next-move`, чтобы не трогать CORS.

---

# Контракты API

**Запрос** `POST /api/v1/next-move`

```json
{
  "size": 7,
  "next": "W",
  "board": [
    ["E","W","E","E","E","E","E"],
    ["E","E","B","E","E","E","E"],
    ["E","E","E","E","E","E","E"],
    ["E","E","E","E","E","E","E"],
    ["E","E","E","E","E","E","E"],
    ["E","E","E","E","E","E","E"],
    ["E","E","E","E","E","E","E"]
  ]
}
```

**Ответ — игра продолжается**

```json
{
  "finished": false,
  "winner": null,
  "move": { "x": 3, "y": 2, "color": "B" },
  "message": "Continue"
}
```

**Ответ — победа**

```json
{
  "finished": true,
  "winner": "W",
  "move": { "x": 2, "y": 2, "color": "W" },
  "message": "Win"
}
```

**Ответ — ничья**

```json
{
  "finished": true,
  "winner": null,
  "move": { "x": 6, "y": 6, "color": "B" },
  "message": "Draw"
}
```

**Ответ — завершена до запроса**

```json
{
  "finished": true,
  "winner": "B",
  "move": null,
  "message": "Game already finished"
}
```

---

# Сборка и запуск

```bash
# из корня backend-проекта:
mvn clean package

# запустить:
mvn spring-boot:run
# по умолчанию: http://localhost:8080

# проверить OpenAPI:
# http://localhost:8080/swagger-ui.html
# http://localhost:8080/v3/api-docs
```

---

# Ключевые решения/паттерны

* **Onion/Clean** : веб-слой не зависит от движка; общение через порт `MoveCalculator`.
* **DIP** : доменный код зависит от абстракции (интерфейс), а не от реализации движка.
* **SRP** :
* `MoveController` — HTTP-приём/валидация/маппинг.
* `CalculateNextMoveUseCase` — бизнес-сценарий «рассчитать ход».
* `EngineMoveCalculator` — интеграция с движком.
* **Алгоритм квадрата** — геометрический поворот вектора на ±90°, `O(k²)` с быстрым membership через `HashSet`.

---

# FAQ / отладка

* **404 Swagger** → нет `springdoc-openapi-starter-webmvc-ui`/не тот URL.
* **CORS ошибка** → включите CORS (см. выше) **или** проксируйте через Next API route.
* **`builder()` не находится** → Maven не видит Lombok как annotation processor (добавьте зависимость со scope `annotationProcessor` и/или `annotationProcessorPaths` в `maven-compiler-plugin`).
* **`Could not find artifact ...`** → зависимость на движок не собрана/не установлена (соберите модуль движка `mvn install`, либо сделайте мульти-модуль и собирайте из корня с `-am`).
