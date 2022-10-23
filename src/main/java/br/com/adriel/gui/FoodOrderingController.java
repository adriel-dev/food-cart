package br.com.adriel.gui;

import br.com.adriel.coreapi.CreateFoodCartCommand;
import br.com.adriel.coreapi.DeselectProductCommand;
import br.com.adriel.coreapi.FindFoodCartQuery;
import br.com.adriel.coreapi.SelectProductCommand;
import br.com.adriel.query.FoodCartView;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/foodCart")
@RestController
public class FoodOrderingController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public FoodOrderingController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/create")
    public CompletableFuture<UUID> createFoodCart() {
        return commandGateway.send(new CreateFoodCartCommand(UUID.randomUUID()));
    }

    @PostMapping("/{foodCartId}/select/{productId}/{quantity}")
    public void selectProduct(@PathVariable("foodCartId") String foodCartId,
                              @PathVariable("productId") String productId,
                              @PathVariable("quantity") Integer quantity) {
        commandGateway.send(new SelectProductCommand(UUID.fromString(foodCartId), UUID.fromString(productId), quantity));
    }

    @PostMapping("/{foodCartId}/deselect/{productId}/{quantity}")
    public void deselectProduct(@PathVariable("foodCartId") String foodCartId,
                                @PathVariable("productId") String productId,
                                @PathVariable("quantity") Integer quantity) {
        commandGateway.send(new DeselectProductCommand(UUID.fromString(foodCartId), UUID.fromString(productId), quantity));
    }

    @GetMapping("/foodCart/{foodCartId}")
    public CompletableFuture<FoodCartView> findFoodCart(@PathVariable("foodCartId") String foodCartId) {
        return queryGateway.query(new FindFoodCartQuery(UUID.fromString(foodCartId)),
                ResponseTypes.instanceOf(FoodCartView.class));
    }

}