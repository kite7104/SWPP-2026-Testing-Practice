# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `@BindingAdapter("app:dueDateLabel") fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Makes the task row's label TextView show exactly the label for the task's **current** state, using today's date (`LocalDate.now()`): the D-day label or "CLEAR" is shown and visible; when there is no label, nothing is shown — even if the same TextView showed a label before (rows are re-bound when a task is checked/unchecked and when RecyclerView recycles rows).

**Pre-conditions**
- `textView` is the `due_date_text` view of a task row (needs Android ⇒ all tests are instrumented).
- The TextView may already hold text/visibility from a previous bind.

**Post-conditions**
- Success cases
  - Active task due in 3 days: text is `"D-3"` and the view is VISIBLE. `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", false, "id1", LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is "D-3" and visibility is VISIBLE
  - Active task overdue by 2 days: text is `"D+2"` and the view is VISIBLE. `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", false, "id1", LocalDate.now().minusDays(2)) / When setDueDateLabel is called / Then text is "D+2" and visibility is VISIBLE
  - Completed task with a due date: text is `"CLEAR"` and VISIBLE. `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", true, "id1", LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is "CLEAR" and visibility is VISIBLE
  - Completed task without a due date: text is `"CLEAR"` and VISIBLE. `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", true, "id1", null) / When setDueDateLabel is called / Then text is "CLEAR" and visibility is VISIBLE
  - Active task without a due date: no label is shown (text is `""` and/or the view is GONE). `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", false, "id1", null) / When setDueDateLabel is called / Then the view is not visible or its text is ""
- Error cases
  - None. A null task is valid (data binding passes null before the item is set). `[Integration]`
- Edge cases
  - `task` is null: no label is shown. `[Integration]`
    - Tip: Given a new TextView and task = null / When setDueDateLabel is called / Then the view is not visible or its text is ""
  - Due today (boundary between D-N and D+N): text is `"D-Day"`. `[Integration]`
    - Tip: Given a new TextView and Task("T", "D", false, "id1", LocalDate.now()) / When setDueDateLabel is called / Then text is "D-Day" and visibility is VISIBLE
  - Re-bind from label → no label (uncheck a completed task with no due date): the old `"CLEAR"` must disappear. `[Integration]`
    - Tip: Given a TextView already bound with Task("T", "D", true, "id1", null) (shows "CLEAR") / When setDueDateLabel is called with Task("T", "D", false, "id1", null) / Then the view is not visible or its text is "" (NOT "CLEAR")
  - Re-bind from a D-day label → no label (recycled row / due date cleared): the old `"D-3"` must disappear. `[Integration]`
    - Tip: Given a TextView already bound with Task("T", "D", false, "id1", LocalDate.now().plusDays(3)) / When setDueDateLabel is called with Task("T", "D", false, "id2", null) / Then the view is not visible or its text is ""
  - Re-bind from no label → label (check an active task with no due date): `"CLEAR"` becomes visible. `[Integration]`
    - Tip: Given a TextView already bound with Task("T", "D", false, "id1", null) / When setDueDateLabel is called with Task("T", "D", true, "id1", null) / Then text is "CLEAR" and visibility is VISIBLE
  - End-to-end in the task list: check then uncheck a task with no due date. `[Integration]`
    - Tip: Given TasksFragment launched with FakeAndroidTestRepository holding Task("TITLE1", "DESC1", false, "id1", null) / When complete_checkbox is clicked twice / Then after the first click due_date_text shows "CLEAR", and after the second click due_date_text does not show "CLEAR"

**Undefined behavior** (optional)
- Midnight rollover while the list is on screen: the label is only recomputed on re-bind, so it may be one day stale until the list refreshes.
