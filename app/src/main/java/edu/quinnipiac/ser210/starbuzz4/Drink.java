package edu.quinnipiac.ser210.starbuzz4;

public class Drink {
    private String name;
    private String description;
    private int imageResourceId;

    public static final Drink[] drinks={
        new Drink("Latte", "A Couple of espresson shots with steamed milk", R.drawable.latte),
                new Drink("Cappuccino", "Espresso, Hot Milk, and steamed milk foam", R.drawable.cappuccino),
    new Drink("Filter", "highest quality beans roasted and brewed fresh", R.drawable.filter)
    };

    private Drink(String name,String description,int imageResourceId)
    {
        this.name=name;
        this.description=description;
        this.imageResourceId=imageResourceId;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public int getImageResourceId()
    {
        return imageResourceId;
    }

    public String toString()
    {
        return this.name;
    }



}
