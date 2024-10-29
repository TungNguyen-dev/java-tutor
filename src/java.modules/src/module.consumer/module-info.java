module module.consumer {
    requires java.base;
    requires module.service;
    uses com.tungnn.java.tutor.modules.service.BusinessService;
}