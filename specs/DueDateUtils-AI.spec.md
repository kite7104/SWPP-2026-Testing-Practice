# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Builds the D-day label shown next to a task in the list. Completed tasks show "CLEAR", active tasks without a due date show nothing, and active tasks with a due date show D-N / D-Day / D+N.

**Pre-conditions**
- `today` is a valid `LocalDate`.

**Post-conditions**
- Success cases
  - Completed task with a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-25, today = 2026-10-20, isCompleted = true / When dueDateLabel is called / Then it returns "CLEAR"
  - Completed task without a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-20, isCompleted = true / When dueDateLabel is called / Then it returns "CLEAR"
  - Active task without a due date returns `""`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns ""
  - Active task due in N days returns `"D-N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-23, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-3"
  - Active task overdue by N days returns `"D+N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+3"
- Error cases
  - None. `[Unit]`
- Edge cases
  - Due today returns `"D-Day"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-20, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-Day"
  - Due tomorrow returns `"D-1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-21, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D-1"
  - Due yesterday returns `"D+1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-19, today = 2026-10-20, isCompleted = false / When dueDateLabel is called / Then it returns "D+1"
  - Due date across a month end returns the calendar-day difference. `[Unit]`
    - Tip: Given dueDate = 2026-11-01, today = 2026-10-31, isCompleted = false / When dueDateLabel is called / Then it returns "D-1"

**Undefined behavior** (optional)
- None.
