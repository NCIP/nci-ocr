package gov.nih.nci.firebird.web;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FirebirdWebModuleTest {

    @Test
    public void testConfigure() {
        InjectionTarget target = Guice.createInjector(new FirebirdWebModule()).getInstance(InjectionTarget.class);
        assertEquals(10, target.paginateMin);
    }

    private static final class InjectionTarget {

        private final int paginateMin;

        @SuppressWarnings("unused")
        @Inject
        InjectionTarget(@Named("firebird.paginate.results.min") int paginateMin) {
            this.paginateMin = paginateMin;
        }

    }

}
