package com.devops.cicd;

public final class PricingService {

    private final PricingConfig config;

    public PricingService(PricingConfig config) {
        this.config = config;
    }

    // Calcule le montant TTC à partir du HT
    public double applyVat(double amountExclVat) {
        return amountExclVat * (1 + config.getVatRate());
    }

    // Applique 10% de remise si le client est VIP
    public double applyVipDiscount(double amount, boolean vip) {
        if (vip) {
            return amount * 0.90;
        }
        return amount;
    }

    // Frais de port : 0 si >= seuil, sinon 4.99
    public double shippingCost(double amount) {
        if (amount >= config.getFreeShippingThreshold()) {
            return 0.0;
        }
        return 4.99;
    }

    /**
     * Calcul final :
     * 1. TVA appliquée d'abord : HT -> TTC
     * 2. Remise VIP appliquée sur le TTC
     * 3. Frais de livraison ajoutés à la fin (basés sur le prix après remise)
     */
    public double finalTotal(double amountExclVat, boolean vip) {
        double ttc = applyVat(amountExclVat);
        double priceAfterVip = applyVipDiscount(ttc, vip);
        double shipping = shippingCost(priceAfterVip);
        
        double total = priceAfterVip + shipping;
    
        // Arrondi à 2 décimales
        return Math.round(total * 100.0) / 100.0;
    }
}