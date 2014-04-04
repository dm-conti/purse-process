package it.paybay.titan.model;


public enum Queues {
	PURSE_QUEUE("purse.in", "purse.out"),
	TICKET_ORDER_QUEUE("ticketOrder.in", "ticketOrder.out"),
	TICKET_TYPES_QUEUE("ticketTypes.in", "ticketTypes.out"),
	TICKET_PURCHASE_QUEUE("ticketPurchase.in", "ticketPurchase.out"),
	TICKET_INFO_QUEUE("ticketInfo.in", "ticketInfo.out"),
	CUSTOMER_INFO_QUEUE("customerInfo.in", "customerInfo.out"),
	NEW_CUSTOMER_QUEUE("newCustomer.in","newCustomer.out"),
	CUSTOMER_SERVICE_ADAPTER_QUEUE("customerServiceAdapter.in","customerServiceAdapter.out"),
	TICKETING_SERVICE_ADAPTER_QUEUE("ticketingServiceAdapter.in","ticketingServiceAdapter.out");
	
	private final String inputQueue, outputQueue;
	
	Queues(String inputQueue, String outputQueue) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}
	
	public String inputQueue() {
		return inputQueue;
	}
	
	public String outputQueue() {
		return outputQueue;
	}
}
