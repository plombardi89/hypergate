package hypergate

import io.envoyproxy.controlplane.cache.*
import io.envoyproxy.controlplane.server.DiscoveryServer
import io.grpc.Server
import io.grpc.netty.NettyServerBuilder
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("hypergate.Hypergate")


fun main(args: Array<String>) {
  val server = createServer()

  server.start()
  logger.info("Hypergate has started 'port = {}'!", server.port)

  Runtime.getRuntime().addShutdownHook(Thread {
    logger.info("Hypergate is shutting down...")
    server.shutdown()
    logger.info("Hypergate has shutdown!")
  })

  server.awaitTermination()
}


private fun createServer(): Server {
  val cache = SimpleCache(null) { _ -> "KEY" }

  val discoveryServer = DiscoveryServer(cache)

  return with(NettyServerBuilder.forPort(7000)) {
    addService(discoveryServer.aggregatedDiscoveryServiceImpl)
    addService(discoveryServer.clusterDiscoveryServiceImpl)
    addService(discoveryServer.endpointDiscoveryServiceImpl)
    addService(discoveryServer.listenerDiscoveryServiceImpl)
    addService(discoveryServer.routeDiscoveryServiceImpl)
    build()
  }
}

