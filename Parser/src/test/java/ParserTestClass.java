import org.example.Grammar;
import org.example.Item;
import org.example.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserTestClass {

    private Parser parser;

    @BeforeEach
    void setUp() throws IOException {
        Grammar grammar = new Grammar();
        grammar.loadFromFile("g1.txt");
        parser = new Parser(grammar);
    }

    @Test
    void testClosure() {
        Set<Item> argument = new HashSet<>();
        Item argumentItem = new Item("S'", new ArrayList<>(), new ArrayList<>(List.of("S")));
        argument.add(argumentItem);
        Set<Item> closureResult = parser.closure(argument);
        assert closureResult.size() == 2;
        assert closureResult.contains(argumentItem);
        Item resultItem = new Item("S", new ArrayList<>(), new ArrayList<>(List.of("a", "A")));
        assert closureResult.contains(resultItem);
    }

    @Test
    void testGoTo() {
        Set<Item> state = new HashSet<>();
        Item argumentItem1 = new Item("S'", new ArrayList<>(), new ArrayList<>(List.of("S")));
        Item argumentItem2 = new Item("S", new ArrayList<>(), new ArrayList<>(List.of("a", "A")));
        state.add(argumentItem1);
        state.add(argumentItem2);
        Set<Item> goToResult = parser.goTo(state, "S");
        assert goToResult.size() == 1;
        Item resultItem = new Item("S'", new ArrayList<>(List.of("S")), new ArrayList<>());
        assert goToResult.contains(resultItem);
    }

    @Test
    void testCanonicalCollection() {
        Set<Set<Item>> canonicalCollection = parser.getCanonicalCollection();
        assert canonicalCollection.size() == 7;
    }
}
