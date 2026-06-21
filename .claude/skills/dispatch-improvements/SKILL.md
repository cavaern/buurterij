---
name: dispatch-improvements
description: Turn a list of feature/improvement requests into GitHub issues, implement each one in parallel via isolated worktree agents, run a supervisor review pass, and open PRs for human review. Invoke manually with /dispatch-improvements.
disable-model-invocation: true
argument-hint: [list of improvements, one per line or paragraph]
---

## Input

Improvements to dispatch:

$ARGUMENTS

If the input above is empty, use the most recent list of improvements the user described earlier in this conversation instead.

## Steps

1. **Verify prerequisites.** Confirm `gh auth status` succeeds and `git remote -v` shows a GitHub remote. If either fails, stop and tell the user what to fix — do not proceed.

2. **Break the input into discrete issues.** Split the input into independently-implementable improvements (roughly: each should be completable as a single focused PR, touching a coherent slice of the codebase, similar in scope to how you'd split a feature list when planning solo). Avoid splitting so finely that pieces depend on each other's code to function, since each will be implemented in an isolated git worktree with no access to the others' changes. If a "feature" only makes sense bundled with another (e.g. a DB schema change and the UI that depends on it), keep them as one issue rather than two.

3. **Show the planned breakdown** to the user as a short numbered list (title only, one line each) before creating anything, so they can see the split. Don't wait for explicit approval unless something in the input was ambiguous enough that a wrong split would waste real work — use judgment.

4. **Create one GitHub issue per item**: `gh issue create --title "<short title>" --body "<description, acceptance criteria if obvious from context>"`. Collect each issue's number and URL.

5. **Dispatch the work via the `/batch` bundled skill.** Invoke it (via the Skill tool) with an instruction that:
   - Lists every issue by number and title
   - Tells each unit to implement exactly one issue's scope, nothing else
   - Tells each unit to reference `Closes #<N>` in its commit message and PR description
   - Reuses existing project patterns/conventions (point it at CLAUDE.md and any relevant existing code, same as you would when briefing a sub-agent yourself)

   Example instruction shape: `/batch Implement these GitHub issues in this repo, one per isolated unit: #12 Add dark mode toggle, #13 Add CSV export, #14 ... For each: implement only that issue's scope, run the project's existing build/test verification, commit with "Closes #<N>" in the message, and open a PR.`

6. **Supervisor review pass.** Once `/batch` has opened PRs, for each PR run `/code-review --comment` (via the Skill tool) targeting that PR/branch, so review findings land as inline PR comments the user will see alongside the diff. If `/code-review` surfaces a clear, low-risk fix, you may apply it directly on that branch before leaving it for the user; for anything uncertain, leave it as a comment rather than guessing.

7. **Final report.** List every issue created (number + title) and every PR opened (number + URL + one-line summary), so the user has a single place to start reviewing. Do not merge anything yourself.
