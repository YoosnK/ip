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

Adds a task that needs to be done by a specific time.

Example: `deadline <description> /by <time>`

```
deadline return book /by Sunday
```

```
I've added the task [[D][ ] return book (by: Sunday)] to your task list
```

## Adding an event

Adds a task that starts and ends at specific times.

Example: `event <description> /from <start> /to <end>`

```
event project meeting /from Mon 2pm /to Mon 4pm
```

```
I've added the task [[E][ ] project meeting (FROM: Mon 2pm; TO: Mon 4pm)] to your task list
```

## Listing all tasks

Shows every task currently tracked, numbered from 1.

Example: `list`

```
1. [T][ ] borrow book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (FROM: Mon 2pm; TO: Mon 4pm)
```

If there are no tasks yet, Nia prints `Nothing here...` instead.

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
