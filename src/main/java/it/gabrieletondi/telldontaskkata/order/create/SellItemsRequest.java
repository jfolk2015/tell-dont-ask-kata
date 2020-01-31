package it.gabrieletondi.telldontaskkata.order.create;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private List<SellItemRequest> requests;

    public SellItemsRequest(List<SellItemRequest> requests) {
        this.requests = new ArrayList<>(requests);
    }

    public List<SellItemRequest> getRequests() {
        return new ArrayList<>(requests);
    }
}
