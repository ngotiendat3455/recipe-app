package guru.springframework.domain;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

public class CategoryTest extends TestCase {
    Category category;
    @Before
    public void setUp(){
        category = new Category();
    }
    @Test
    public void testGetId() {
        Long idCategory = 4L;
        category.setId(idCategory);
        assertEquals(idCategory, category.getId());
    }

    @Test
    public void testGetDescription() {
    }

    @Test
    public void testGetRecipes() {
    }
}
