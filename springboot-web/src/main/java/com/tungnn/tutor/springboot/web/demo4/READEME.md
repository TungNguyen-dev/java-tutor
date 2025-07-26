## Controllers and ViewResolvers

Spring MVC Controllers handle web requests and can use ViewResolvers to render views. Thymeleaf is a
modern server-side Java template engine that needs to be configured in the project.

### Key components:

- Controllers process web requests and prepare model data
- ViewResolvers map logical view names to actual view templates
- Thymeleaf integration requires appropriate dependencies and configuration

## ModelAndView

ModelAndView is a Spring class that encapsulates both:

- Model data to be displayed in the view
- The logical view name

It provides a flexible way to return both model and view information from controller methods.