package warehouse_management.acceptance.support

import io.cucumber.java.Before

class Hooks(private val testContext: TestContext) {

    @Before
    fun setUp() {
        testContext.reset()
    }
}