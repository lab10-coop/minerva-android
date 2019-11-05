package coop.lab10.minerva.events

// for EventBus
class ErrorEvent(val message: String)
class MessageEvent(val message: String)
class PaymentStartedEvent()
class PaymentErrorEvent(val message: String)