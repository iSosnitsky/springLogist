package sbat.logist.ru.constant;


import lombok.Getter;

public enum RequestStatus {
    ADJUSTMENTS_MADE("Сделаны документы"),
    APPROVED("Утверждена к сборке"),
    APPROVING("Выгружена на утверждение торговому представителю"),
    ARRIVED("Накладная прибыла в пункт"),
    CHECK("На контроле"),
    CHECK_BOXES("Проверка коробок в зоне отгрузки"),
    CHECK_PASSED("Контроль пройден"),
    COLLECTING("Выдана на сборку"),
    CREATED("Заявка добавлена в БД"),
    CREDIT_LIMIT("Кредитный лимит"),
    DELETED("Заявка удалена из БД"),
    DELIVERED("Доставлено"),
    DEPARTURE("В транзите"),
    ERROR("Не доставлено"),
    PACKAGING("Упаковано"),
    PRETENSION_CLOSED("Претензия удовлетворена"),
    PRETENSION_CREATED("Создана претензия"),
    RASH_CREATED("Создана расходная накладная"),
    READY("Проверка в зоне отгрузки/Готова к отправке"),
    RESERVED("Резерв"),
    SAVED("Заявка в состоянии черновика"),
    STOP_LIST("Стоп-лист"),
    TRANSPORTATION("Маршрутный лист закрыт, товар передан экспедитору на погрузку"),
    UNKNOWN("Неизвестный статус"),
    UPDATED("заявка обновлена из 1С");

    @Getter
    private final String rusRequestStatusName;

    RequestStatus(String rusRequestStatusName) {
        this.rusRequestStatusName = rusRequestStatusName;
    }

}


