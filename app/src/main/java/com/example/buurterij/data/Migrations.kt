package com.example.buurterij.data

import androidx.room.migration.Migration

/**
 * Real migrations for every schema version bump from here on, so installs on real devices keep
 * their data across updates. Add one entry per version jump, e.g. for bumping version 4 -> 5:
 *
 * private val MIGRATION_4_5 = Migration(4, 5) { db ->
 *     db.execSQL("ALTER TABLE journal_entries ADD COLUMN pinned INTEGER NOT NULL DEFAULT 0")
 * }
 *
 * then add MIGRATION_4_5 to the array below. Verify new entries against the schema JSON files
 * Room writes to app/schemas/ on build (room.schemaLocation in app/build.gradle.kts).
 */
val MIGRATIONS: Array<Migration> = arrayOf()
