package com.devops.cicd;

/**
 * Classe de configuration pour les paramètres de tarification.
 * Utilise 'final' pour garantir l'immutabilité des paramètres une fois fixés.
 */
public class PricingConfig {
    private final double vatRate;
    private final double freeShippingThreshold;

    public PricingConfig(double vatRate, double freeShippingThreshold) {
        this.vatRate = vatRate;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public double getVatRate() {
        return vatRate;
    }

    public double getFreeShippingThreshold() {
        return freeShippingThreshold;
    }
}