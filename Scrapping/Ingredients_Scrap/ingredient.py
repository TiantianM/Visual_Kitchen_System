class Ingredient:
    def __init__(self, name, scientific_name, food_group, food_subgroup, img):
        self.name = name
        self.scientific_name = scientific_name
        self.food_group = food_group
        self.food_subgroup = food_subgroup
        self.img = img

    def __str__(self):
        return f"Ingredient: {self.name}\n" \
               f"Scientific Name: {self.scientific_name}\n" \
               f"Food Group: {self.food_group}\n" \
               f"Food Subgroup: {self.food_subgroup}\n" \
               f"Image URL: {self.img}"