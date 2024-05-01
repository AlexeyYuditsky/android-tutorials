package com.alexeyyuditsky.unittests

import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceManagerTestJunit {

    @Test
    fun `consumeResource after setResource call receives resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()
        val resource = "TEST"

        resourceManager.setResource(resource)
        resourceManager.consumeResource(consumer)

        assertEquals(resource, consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun `consumeResource calls after setResource receive resource for each consumer`() {
        val resourceManager = createResourceManager()
        val listConsumer = List(10) { TestConsumer() }
        val resource = "TEST"

        resourceManager.setResource(resource)
        listConsumer.forEach { consumer ->
            resourceManager.consumeResource(consumer)
        }

        listConsumer.forEach { consumer ->
            assertEquals(resource, consumer.lastResource)
            assertEquals(1, consumer.invokeCount)
        }
    }

    @Test
    fun `consumeResource after setResource call receives latest resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()
        val resource1 = "TEST"
        val resource2 = "TEST_SECOND"

        resourceManager.setResource(resource1)
        resourceManager.setResource(resource2)
        resourceManager.consumeResource(consumer)

        assertEquals(resource2, consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun `consumeResource calls with same consumer can receive the same resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()
        val resource = "TEST"

        resourceManager.setResource(resource)
        resourceManager.consumeResource(consumer)
        resourceManager.consumeResource(consumer)

        assertEquals(resource, consumer.resources.first())
        assertEquals(resource, consumer.resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun `consumeResource without active resource does nothing`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun `setResource after consumeResource call delivers resource to consumer`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()
        val resource = "TEST"

        resourceManager.consumeResource(consumer)
        resourceManager.setResource(resource)

        assertEquals(1, consumer.invokeCount)
        assertEquals(resource, consumer.lastResource)
    }

    @Test
    fun `consumeResource receives resource only once`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals("TEST1", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun `consumerResource calls with same consumer can receive multiple resources`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun `setResource after multiple consumeResource calls delivers resource to all consumers`() {
        val resourceManager = createResourceManager()
        val consumerList = List(10) { TestConsumer() }

        consumerList.forEach { consumer ->
            resourceManager.consumeResource(consumer)
        }
        resourceManager.setResource("TEST")

        consumerList.forEach { consumer ->
            assertEquals("TEST", consumer.lastResource)
            assertEquals(1, consumer.invokeCount)
        }
    }

    @Test
    fun `setResource calls after consumeResource call delivers the first resource once`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST1", consumer.lastResource)
    }

    @Test
    fun `setResource call between consumeResource calls delivers the same resource to all consumers`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST", consumer.resources[0])
        assertEquals("TEST", consumer.resources[1])
    }

    @Test
    fun `setResource double call between consumeResource calls delivers different resources`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
    }

    @Test
    fun `consumeResource after clearResource call`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun `consumeResource after clearResource and setResource calls receives latest resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun `setResource call after consumeResource and clearResource calls delivers latest resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun `destroy clears current resource`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.destroy()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun `destroy clears pending consumers`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.destroy()
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun `setResource after destroy call does nothing`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.destroy()
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun `consumeResource after destroy call does nothing`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.destroy()
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test(expected = Test.None::class)
    fun `setResource handles concurrent consumers modification`() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource {
            resourceManager.clearResource()
            resourceManager.consumeResource(consumer)
        }
        resourceManager.setResource("TEST")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun `consumeResource delivers exceptions to error handler`() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(errorHandler = errorHandler)
        val expectedException = IllegalStateException("Test exception")

        resourceManager.setResource("TEST")
        resourceManager.consumeResource { resource ->
            throw expectedException
        }

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    @Test
    fun `setResource delivers exceptions to error handler`() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(errorHandler = errorHandler)
        val expectedException = IllegalStateException("Test exception")

        resourceManager.consumeResource { resource ->
            throw expectedException
        }
        resourceManager.setResource("TEST")

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    @Test
    fun `setResource delivers exceptions to error handler and further operation of consumer in normal mode`() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(errorHandler = errorHandler)
        val expectedException = IllegalStateException("Test exception")
        val consumer = TestConsumer()

        resourceManager.consumeResource { resource ->
            throw expectedException
        }
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun `consumeResource invokes consumer in executor`() {
        val executor = TestExecutor()
        val resourceManager = createResourceManager(executor = executor)
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(1, executor.invokeCount)
    }

    @Test
    fun `consumeResource resource invokes consumer in executor`() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(executor = executor)
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
        assertEquals(1, executor.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals(1, executor.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun `setResource invokes pending consimer in executor`() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(executor = executor)
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
        assertEquals(1, executor.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals(1, executor.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    private fun createResourceManager(
        executor: TestExecutor = TestExecutor(),
        errorHandler: TestErrorHandler = TestErrorHandler()
    ): ResourceManager<String> = ResourceManager(
        executor = executor,
        errorHandler = errorHandler
    )
}