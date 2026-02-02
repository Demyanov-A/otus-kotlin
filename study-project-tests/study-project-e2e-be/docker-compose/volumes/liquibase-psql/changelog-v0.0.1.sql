--liquibase formatted sql

--changeset sokatov:1 labels:v0.0.1
CREATE TYPE "task_status_type" AS ENUM ('new', 'toDo', 'inProgress', 'canceled', 'hold', 'done');
CREATE TYPE "task_category_type" AS ENUM ('hi', 'medium', 'low', 'personal');

CREATE TABLE "tasks" (
                         "id" text primary key constraint tasks_id_length_ctr check (length("id") < 64),
                         "title" text constraint tasks_title_length_ctr check (length(title) < 128),
                         "description" text constraint tasks_description_length_ctr check (length(title) < 4096),
                         "status" task_status_type not null,
                         "category" task_category_type not null,
                         "user_id" text not null constraint tasks_user_id_length_ctr check (length(id) < 64),
                         "deadline" text constraint tasks_deadline_length_ctr check (length(id) < 64),
                         "dtCreate" text constraint tasks_dtCreate_length_ctr check (length(id) < 64),
                         "lock" text not null constraint tasks_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX tasks_user_id_idx on "tasks" using hash ("user_id");

CREATE INDEX tasks_deadline_idx on "tasks" using hash ("deadline");

CREATE INDEX tasks_dtCreate_idx on "tasks" using hash ("dtCreate");

CREATE INDEX task_status_idx on "tasks" using hash ("status");

CREATE INDEX task_category_idx on "tasks" using hash ("category");
