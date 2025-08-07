/**
 * Provides a comprehensive set of task scheduling utilities
 * with support for one-time and recurring task execution.
 * <p>
 * Tasks are uniquely identified by numeric IDs and
 * can be scheduled with configurable delays and intervals
 * using {@link org.akazukin.util.time.TimeHolder} for time specification.
 * All scheduler implementations provide exception handling
 * mechanisms through configurable {@link java.util.function.Consumer} instances
 * and support graceful shutdown for lifecycle management.
 * <p>
 * The scheduling infrastructure automatically handles task
 * lifecycle events, including cancellation, completion tracking, and resource cleanup.
 */
package org.akazukin.util.time.scheduler;
