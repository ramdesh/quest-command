package traveling.location.location

class LocationsGenerated : LocationsCollection {
    override val values = listOf<LocationResource>(resources.traveling.location.location.CommonLocations(), resources.traveling.location.location.KanbaraCountrysideLocations()).flatMap { it.values }
}