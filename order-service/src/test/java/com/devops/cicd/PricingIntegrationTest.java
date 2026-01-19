package com.devops.cicd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PricingIntegrationTest {

    @Test
    void fullPricingFlow_withRealConfigFile_VipCustomer() {
        // 1. Charger la configuration réelle
        PricingConfigLoader loader = new PricingConfigLoader();
        PricingConfig realConfig = loader.load();
        PricingService service = new PricingService(realConfig);

        // 2. Scénario VIP : 100€ HT
        // 100€ + 20% TVA = 120€
        // Remise VIP 10% sur 120€ = 108€
        // 108€ > 50€ (seuil) -> Livraison offerte (0€)
        double result = service.finalTotal(100.0, true);

        assertEquals(108.0, result, "Le total VIP devrait être de 108.0");
    }

    @Test
    void fullPricingFlow_withRealConfigFile_StandardCustomer_SmallAmount() {
        // 1. Configuration
        PricingConfigLoader loader = new PricingConfigLoader();
        PricingConfig realConfig = loader.load();
        PricingService service = new PricingService(realConfig);

        // 2. Scénario Standard : 10€ HT
        // 10€ + 20% TVA = 12€
        // Pas de remise VIP -> 12€
        // 12€ < 50€ (seuil) -> Frais de port + 4.99€
        // Total : 12 + 4.99 = 16.99€
        double result = service.finalTotal(10.0, false);

        assertEquals(16.99, result, "Le total Standard avec livraison devrait être de 16.99");
    }
}