package com.devops.cicd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    // Configuration contrôlée : TVA à 20% (0.20) et livraison gratuite à partir de 50€
    private final PricingConfig fakeConfig = new PricingConfig(0.20, 50.0);
    private final PricingService service = new PricingService(fakeConfig);

    @Test
    void shouldApplyVatCorrecty() {
        // Étant donné 100€ HT, avec une TVA à 20%, on attend 120€ TTC
        double result = service.applyVat(100.0);
        assertEquals(120.0, result, "La TVA de 20% devrait transformer 100€ en 120€");
    }

    @Test
    void shouldApplyVipDiscountWhenUserIsVip() {
        // Étant donné 100€ TTC, un VIP doit payer 90€ (10% de remise)
        double result = service.applyVipDiscount(100.0, true);
        assertEquals(90.0, result, "Le client VIP devrait bénéficier de 10% de remise");
    }

    @Test
    void shouldNotApplyDiscountWhenUserIsNotVip() {
        // Étant donné 100€ TTC, un non-VIP doit payer 100€
        double result = service.applyVipDiscount(100.0, false);
        assertEquals(100.0, result, "Le client non-VIP ne devrait pas avoir de remise");
    }

    @Test
    void shouldReturnZeroShippingCostWhenAmountIsExactlyThreshold() {
        // Au seuil exact (50€), la livraison est offerte
        double result = service.shippingCost(50.0);
        assertEquals(0.0, result, "La livraison doit être gratuite à partir de 50€");
    }

    @Test
    void shouldApplyShippingFeesWhenAmountIsBelowThreshold() {
        // En dessous de 50€, on paie 4.99€
        double result = service.shippingCost(49.99);
        assertEquals(4.99, result, "Les frais de port devraient être de 4.99€ en dessous du seuil");
    }

    @Test
    void shouldCalculateFinalTotalForVipWithFreeShipping() {
        // Calcul complet : 
        // 100€ HT -> 120€ TTC (TVA 20%)
        // 120€ TTC -> 108€ (Remise VIP 10%)
        // 108€ >= 50€ -> Livraison 0€
        // Total attendu : 108€
        double result = service.finalTotal(100.0, true);
        assertEquals(108.0, result);
    }

    @Test
    void shouldCalculateFinalTotalForNonVipWithShippingFees() {
        // Calcul complet : 
        // 10€ HT -> 12€ TTC (TVA 20%)
        // 12€ TTC -> 12€ (Pas de remise)
        // 12€ < 50€ -> Livraison 4.99€
        // Total attendu : 16.99€
        double result = service.finalTotal(10.0, false);
        assertEquals(16.99, result);
    }
}