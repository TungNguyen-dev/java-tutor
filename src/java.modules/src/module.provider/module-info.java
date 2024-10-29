module module.provider {
    requires java.base;
    requires module.service;
    provides com.tungnn.java.tutor.modules.service.BusinessService with com.tungnn.java.tutor.modules.service.impl.EmailService;
}