package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IngredientServiceImpTest extends TestCase {
    IngredientServiceImp ingredientServiceImp;

    @Mock
    public RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpTest() {
        // this.recipeRepository = recipeRepository;
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }
    @Before
    public void setUp() throws Exception {
        // super.setUp();
        MockitoAnnotations.initMocks(this);
        ingredientServiceImp = new IngredientServiceImp(recipeRepository, ingredientToIngredientCommand);
    }


    @Test
    public void testFindByRecipeIdAndIngredientId() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient1.setId(2L);
        Ingredient ingredient3 = new Ingredient();
        ingredient1.setId(3L);
        HashSet<Ingredient> ingredientHashSet = new HashSet<>();
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        Mockito.when(recipeRepository.findById(1L)).thenReturn(recipeOptional);
        IngredientCommand ingredientCommand = ingredientServiceImp.findByRecipeIdAndIngredientId(1L, 3L);
        assertEquals(ingredientCommand.getId(), Long.valueOf(3L));
        assertEquals(ingredientCommand.getRecipeId(), Long.valueOf(1L));
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(Mockito.any());
    }
}
