package itacad.aliaksandrkryvapust.myfitapp.manager.api;

public interface IManagerUpdate <TYPE, TYPE2> {
    TYPE update(TYPE2 type, Long id, Integer version);
}
