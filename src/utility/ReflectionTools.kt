package utility

import commands.Command
import events.Event
import events.EventListener
import org.reflections.Reflections
import processing.use.Use


object ReflectionTools {

    fun getAllCommands() : List<Class<out Command>> {
        val reflections = Reflections("commands")
        val allClasses = reflections.getSubTypesOf(Command::class.java)
        return allClasses.toList()
    }

    fun getAllEvents() : List<Class<out Event>> {
        val reflections = Reflections("events")
        val allClasses = reflections.getSubTypesOf(Event::class.java)
        return allClasses.toList()
    }

    fun getAllUses() : List<Class<out Use>> {
        val reflections = Reflections("processing.use")
        val allClasses = reflections.getSubTypesOf(Use::class.java)
        return allClasses.toList()
    }

    fun getAllEventListeners() : List<Class<out EventListener<*>>> {
        val reflections = Reflections("processing")
        val allClasses = reflections.getSubTypesOf(EventListener::class.java)
        return allClasses.toList()
    }
}