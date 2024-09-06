# define the structure of the recipe
class Recipe:
    def __init__(self, title, img_src, ready_in, ingredient_num, ingredients, directions):
        self.title = title
        self.img_src = img_src
        self.ready_in = ready_in  # estimated preparation time
        self.ingredient_num = ingredient_num  # number of ingredients needed
        self.ingredients = ingredients  # list of ingredients
        self.directions = directions  # step by step instructions

    def __str__(self):
        return f"Recipe: {self.title}\n" \
               f"Image Source: {self.img_src}\n" \
               f"Ready in: {self.ready_in} minutes\n" \
               f"Number of Ingredients: {self.ingredient_num}\n" \
               f"Ingredients: {', '.join(self.ingredients)}\n" \
               f"Directions: {self.directions}"