package org.poo.commerciants;

import org.poo.fileio.CommerciantInput;

import java.util.List;

public class CommerciantType {
    final private int id;
    final private String description;
    final private List<String> commerciants;
    public CommerciantType(final CommerciantInput commerciantInput) {
        id = commerciantInput.getId();
        description = commerciantInput.getDescription();
        commerciants = commerciantInput.getCommerciants();
    }
}
