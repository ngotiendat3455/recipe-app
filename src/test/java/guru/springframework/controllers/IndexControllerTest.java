package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import guru.springframework.services.RecipeServiceImp;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IndexControllerTest extends TestCase {
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    IndexController indexController;

    @Before
    public void setUp() throws Exception {
        // super.setUp();
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }


    public void testMockMvc() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void testGetIndexPage() {
        String viewName = indexController.getIndexPage(model);
        assertEquals("index", viewName);
        Set<Recipe> recipeHashSet = new HashSet<>();
         // Recipe recipe = new Recipe();

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipeHashSet.add(recipe);
        recipeHashSet.add(new Recipe());
        Mockito.when(recipeService.getRecipes()).thenReturn(recipeHashSet);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        Mockito.verify(recipeService, Mockito.times(1)).getRecipes();
        Mockito.verify(model, Mockito.times(1)).addAttribute(Mockito.eq("recipes"), argumentCaptor.capture());
        Set<Recipe> getArgumentCaptor = argumentCaptor.getValue();
        // assertEquals(2, getArgumentCaptor.size());
    }
}
