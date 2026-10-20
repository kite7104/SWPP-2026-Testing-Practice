# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Binding adapter that shows the D-day label of a task in the task list's `TextView`, using today's date.

**Pre-conditions**
- `textView` is a valid `TextView`.

**Post-conditions**
- Success cases
  - Active task with a due date: text is set to the D-day label and the view is visible. `[Unit]`
    - Tip: Given Task("T", "D", false, dueDate = today + 3) / When setDueDateLabel is called / Then text is "D-3" and visibility is VISIBLE
  - Completed task: text is set to "CLEAR" and the view is visible. `[Unit]`
    - Tip: Given Task("T", "D", true, dueDate = null) / When setDueDateLabel is called / Then text is "CLEAR" and visibility is VISIBLE
  - Active task without a due date: the TextView is not updated. `[Unit]`
    - Tip: Given Task("T", "D", false, dueDate = null) / When setDueDateLabel is called / Then text is ""
- Error cases
  - None. `[Unit]`
- Edge cases
  - `task` is null: the TextView is not updated. `[Unit]`
    - Tip: Given task = null / When setDueDateLabel is called / Then text is ""
  - Active task due today: text is "D-Day". `[Unit]`
    - Tip: Given Task("T", "D", false, dueDate = today) / When setDueDateLabel is called / Then text is "D-Day"

**Undefined behavior** (optional)
- None.
