# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Returns the D-day label for one task. A completed task is always `"CLEAR"`; an active task with no due date has no label (`""`); an active task with a due date shows N = the **total number of calendar days** from `today` to `dueDate` (not the day-of-month part): `"D-N"` if due in the future, `"D-Day"` if due today, `"D+N"` if overdue.

**Pre-conditions**
- `today` is the caller's current local date (non-null). `dueDate` may be null.

**Post-conditions**
- Success cases
  - Completed task with a due date returns `"CLEAR"`, even when it is overdue. `[Unit]`
    - Tip: Given dueDate = 2026-10-15, today = 2026-10-20, isCompleted = true / When dueDateLabel is called / Then it returns "CLEAR"
  - Completed task with no due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-20, isCompleted = true / When dueDateLabel is called / Then it returns "CLEAR"
  - Active task with no due date returns `""` (no label). `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns ""
  - Active task due in N days (same month) returns `"D-N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-23, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-3"
  - Active task due in N days where N ≥ 1 month returns `"D-N"` with N in total days. `[Unit]`
    - Tip: Given dueDate = 2026-11-25, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-36"
  - Active task overdue by N days (same month) returns `"D+N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+3"
  - Active task overdue by N days where N ≥ 1 month returns `"D+N"` with N in total days. `[Unit]`
    - Tip: Given dueDate = 2026-09-10, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+40"
- Error cases
  - None. Every combination of inputs is valid. `[Unit]`
- Edge cases
  - Boundary D-Day / D-N / D+N (day 0):
    - Due today returns `"D-Day"`. `[Unit]`
      - Tip: Given dueDate = 2026-10-20, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-Day"
    - Due tomorrow returns `"D-1"`. `[Unit]`
      - Tip: Given dueDate = 2026-10-21, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-1"
    - Due yesterday returns `"D+1"`. `[Unit]`
      - Tip: Given dueDate = 2026-10-19, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+1"
  - Boundary at exactly one month (difference is a whole month, day part is 0) — must NOT be "D-Day":
    - Due exactly one calendar month later returns `"D-31"`. `[Unit]`
      - Tip: Given dueDate = 2026-11-20, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-31"
    - One day before that returns `"D-30"`. `[Unit]`
      - Tip: Given dueDate = 2026-11-19, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-30"
    - One day after that returns `"D-32"`. `[Unit]`
      - Tip: Given dueDate = 2026-11-21, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-32"
    - Overdue by exactly one calendar month returns `"D+30"`. `[Unit]`
      - Tip: Given dueDate = 2026-09-20, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+30"
  - Month end:
    - Due on the first day of next month returns `"D-1"`. `[Unit]`
      - Tip: Given dueDate = 2026-11-01, today = 2026-10-31, isCompleted = false / When dueDateLabel is called / Then it returns "D-1"
  - Year end:
    - Due on Jan 1 of next year returns `"D-1"`. `[Unit]`
      - Tip: Given dueDate = 2027-01-01, today = 2026-12-31, isCompleted = false / When dueDateLabel is called / Then it returns "D-1"
    - Due exactly one year later returns `"D-365"`. `[Unit]`
      - Tip: Given dueDate = 2027-10-20, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-365"
  - Leap day:
    - Across Feb 29 in a leap year returns `"D-2"`. `[Unit]`
      - Tip: Given dueDate = 2028-03-01, today = 2028-02-28, isCompleted = false / When dueDateLabel is called / Then it returns "D-2"
    - From Jan 31 to Mar 1 in a leap year returns `"D-30"`. `[Unit]`
      - Tip: Given dueDate = 2028-03-01, today = 2028-01-31, isCompleted = false / When dueDateLabel is called / Then it returns "D-30"
  - Completed task due today returns `"CLEAR"` (completion wins over D-Day). `[Unit]`
    - Tip: Given dueDate = 2026-10-20, today = 2026-10-20, isCompleted = true / When dueDateLabel is called / Then it returns "CLEAR"

**Undefined behavior** (optional)
- Time zone: the function only compares `LocalDate`s; which zone defines "today" is the caller's responsibility.
