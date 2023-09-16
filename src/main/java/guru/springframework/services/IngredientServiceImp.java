package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImp implements IngredientService{
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    public IngredientServiceImp(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasureRepository unitOfMeasureRepository, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (!recipe.isPresent()){
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }
        Optional<IngredientCommand> ingredient = recipe.get().getIngredients().stream()
                .filter(ingredient1 -> ingredient1.getId().equals(ingredientId))
                .map(ingredient1 -> ingredientToIngredientCommand.convert(ingredient1))
                .findFirst();

        if(!ingredient.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredient.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
        if (!recipeOptional.isPresent()){
            //todo impl error handling
            log.error("recipe id not found. Id: " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId()).orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                ingredient.setRecipe(recipe);
                recipe.getIngredients().add(ingredient);
            }
            Recipe savedRecipe = recipeRepository.save(recipe);
            Optional<Ingredient> optionalIngredient = savedRecipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();
            if (!optionalIngredient.isPresent()){
                optionalIngredient = savedRecipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(ingredient -> ingredient.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(ingredient -> ingredient.getUom().getId().equals(ingredientCommand.getUom().getId()))
                        .findFirst();

            }
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        log.debug("deleteById id: " + recipeId + " " + ingredientId);
        if (recipe.isPresent()){
            log.debug("recipe.isPresent id: " + recipeId + " " + ingredientId);
            Optional<Ingredient> ingredient = recipe.get().getIngredients().stream()
                    .filter(ingredient1 -> ingredient1.getId().equals(ingredientId))
                    .findFirst();
            if (ingredient.isPresent()){
                Recipe recipeOptional = recipe.get();
                Ingredient ingredientToDelete = ingredient.get();
                ingredientToDelete.setRecipe(null);
                recipeOptional.getIngredients().remove(ingredientToDelete);
                log.debug("ingredient.isPresent excecute");
                recipeRepository.save(recipeOptional);
            } else {
                log.debug("Ingredient id not found: " + ingredientId);
            }
        } else {
            log.debug("Ingredient id not found: " + recipeId);
        }


    }
}
