# Nia User Guide

> **Note:** Commands are case-sensitive. For example, `list` will be recognized, but `List` or `LIST` will not.

Nia is a CLI task tracker. Add todos, deadlines, and events, then list, mark, and
unmark them as you go.

```
 _   _   ___      _    
| \ | | |_ _|    / \   
|  \| |  | |    / _ \  
| |\  |  | |   / ___ \ 
|_| \_| |___| /_/   \_\

Hi, my name's Nia.
How can I help you?
```

## Adding a todo

Adds a task with just a description - no date/time attached.

Example: `todo <description>`

```
todo borrow book
```

```
I've added the task [[T][ ] borrow book] to your task list
```

## Adding a deadline

Adds a task that needs to be done by a specific date and time.

Example: `deadline <description> /by <date-time>`

```
deadline return book /by 2019-10-15 1800
```

```
I've added the task [[D][ ] return book (by: Oct 15 2019, 6:00pm)] to your task list
```

## Adding an event

Adds a task that starts and ends at specific dates and times.

Example: `event <description> /from <date-time> /to <date-time>`

```
event project meeting /from 2024-03-11 1400 /to 2024-03-11 1600
```

```
I've added the task [[E][ ] project meeting (from: Mar 11 2024, 2:00pm; to: Mar 11 2024, 4:00pm)] to your task list
```

### Date-time format

A `<date-time>` (deadline's `/by`, event's `/from` and `/to`) must be:

- a date, with `-` or `/` as the separator: `yyyy-MM-dd` or `yyyy/MM/dd`
- optionally followed by a time, either `HHmm` or `HH:mm` (24-hour)

If no time is given, it defaults to **23:59** that day. For example, all of
these are accepted:

```
2019-10-15 1800
2019-10-15 18:00
2019/10/15 1800
2019/10/15 18:00
2019-10-15          (defaults to 2019-10-15 23:59)
2019/10/15          (defaults to 2019/10/15 23:59)
```

Anything else (e.g. `Sunday`, `next week`) is rejected with an error.

## Listing all tasks

Shows every task currently tracked, numbered from 1.

Example: `list`

```
1. [T][ ] borrow book
2. [D][ ] return book (by: Oct 15 2019, 6:00pm)
3. [E][ ] project meeting (from: Mar 11 2024, 2:00pm; to: Mar 11 2024, 4:00pm)
```

If there are no tasks yet, Nia prints `Nothing here...` instead.

### Filtering the list by type

Add a filter word after `list` to show only todos, deadlines, or events.
Indices still reflect each task's position in the full list, so they stay
valid for `mark`/`unmark`/`delete`.

Example: `list <filter>`, where `<filter>` is one of:

| Filter | Shows |
|---|---|
| `t`, `todo` | todos only |
| `d`, `dl`, `deadline` | deadlines only |
| `e`, `event` | events only |

```
list t
```

```
1. [T][ ] borrow book
```

## Finding tasks

Shows every task whose description contains a keyword (case-insensitive).

Example: `find <keyword>`

```
find book
```

```
1. [T][ ] borrow book
2. [D][ ] return book (by: Oct 15 2019, 6:00pm)
```

If nothing matches, Nia prints `Nothing here...` instead.

## Deleting a task

Removes a task by its number in `list`.

Example: `delete <task number>`

```
delete 1
```

```
Ugh, fine, I've thrown out this task:
    [T][ ] borrow book
Now you have 2 tasks left in your list.
```

## Marking / unmarking a task

Marks a task done, or reverts it back to not done, by its number in `list`.

Example: `mark <task number>` / `unmark <task number>`

```
mark 1
```

```
Marked 1 as done
```

```
unmark 1
```

```
Marked 1 as not done
```

## Getting help

Prints a quick-reference summary of every command.

Example: `help`

## Exiting

Ends the program.

Example: `bye`

## Command aliases

A few shorter or alternate spellings work in place of the full command word:

| Alias | Canonical command |
|---|---|
| `close`, `exit`, `quit` | `bye` |
| `td` | `todo` |
| `dl` | `deadline` |
| `ls` | `list` |
| `d` | `delete` |
