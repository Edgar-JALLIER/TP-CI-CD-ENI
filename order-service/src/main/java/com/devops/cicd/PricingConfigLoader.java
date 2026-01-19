package com.devops.cicd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PricingConfigLoader {

    public PricingConfig load() {
        Properties props = new Properties();
        
        // On charge le fichier depuis le "classpath" (dossier resources)
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("Désolé, impossible de trouver app.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors de la lecture du fichier de config", ex);
        }

        // On extrait les valeurs en utilisant notre méthode utilitaire
        double vat = Double.parseDouble(required(props, "vatRate"));
        double threshold = Double.parseDouble(required(props, "freeShippingThreshold"));

        return new PricingConfig(vat, threshold);
    }

    // Méthode utilitaire pour vérifier que la clé existe bien dans le fichier
    private String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("La configuration '" + key + "' est manquante !");
        }
        return value;
    }
}