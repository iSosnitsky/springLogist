package sbat.logist.ru.parser.command;

public interface Command<IN, OUT> {
    OUT execute(IN in);
}
