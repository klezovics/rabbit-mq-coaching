

         // Concepts
         Elements: Producer-Broker-Consumer-Messages

         Producer --sends to--> Exchange
         Exchange -> Inbox
         Topic/Fanout/Direct

         Bindings -> Link between queue and exchange
         Routing key -> helps decide how to route message to queue
         VHost -> env segregation
         Consumer --subscribes-to--> Queue

         Exchange -> Binding -> Queue

         Connection -> TCP connection
         Channel -> virtual connection

         // Classes
         ConnectionFactory -> builds connections
         Connection -> factory.build(connection-string)
         Channel -> connection.getChannel() -> virtual connection
         Queue -> channel.declareQueue("my-cool-queue", ...)

         //Send message
         channel.basicPublish("my-cool-queue", "My mssg", ...)

         //Cleanup
         channel.close()
         connection.close()

         //Receive message
         channel.basicConsume("my-cool-queue", true, deliverCallback, cancelCallback)
     

Direct exchange -> Route based on FULL value of routing key 
Fanout exchange -> Routes to ALL bound queues
Topic  exchange -> Route based on REGEXP of routing key 
Header exchange -> Route based on HEADER values