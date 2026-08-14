🎯 **What:** Removed unused `java.util.Optional` import from `NotificationServiceImpl.java`.
💡 **Why:** Standard hygiene check to improve readability and maintainability by removing dead code.
✅ **Verification:** Ran `mvn clean test -DskipTests=true` to verify the code compiles without errors. There are no tests for `NotificationServiceImpl.java` to run.
✨ **Result:** A cleaner file with no unused imports.
