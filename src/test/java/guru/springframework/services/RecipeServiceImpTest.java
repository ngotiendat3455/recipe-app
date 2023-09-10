package guru.springframework.services;

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
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeServiceImpTest extends TestCase {
    RecipeServiceImp recipeServiceImp;
    @Mock
    public RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        // super.setUp();
        MockitoAnnotations.initMocks(this);
        recipeServiceImp = new RecipeServiceImp(recipeRepository);
    }

    @Test
    public void testGetRecipes() {
        Recipe recipe = new Recipe();
        Set<Recipe> recipeHashSet = new HashSet<>();
        recipeHashSet.add(recipe);
        Mockito.when(recipeServiceImp.getRecipes()).thenReturn(recipeHashSet);
        Set<Recipe> recipes = recipeServiceImp.getRecipes();
        assertEquals(1, recipes.size());
        Mockito.verify(recipeRepository, Mockito.times(1)).findAll();
    }
}
