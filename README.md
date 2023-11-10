# ShowControl
A plugin to easily create shows with multiple elements like Animatronics, Fireworks, FlameThrowers, Spotlights, Lasers, and more!

## Usage
Check out the description on [Spigot](https://www.spigotmc.org/resources/showcontrol.112641/) or [Polymart](https://polymart.org/resource/showcontrol.4793) for more information.

## Developer API
This project contains a Developer API, which makes it possible to add your own show elements. For more information, check out the [JavaDoc](https://sbdevelopment.tech/javadoc/showcontrol/).

### Including using Maven
The project is on our Maven repository, add the following to your `pom.xml` file:
```xml
<repository>
  <id>sbdevelopment-repo-releases</id>
  <name>SBDevelopment Repository</name>
  <url>https://repo.sbdevelopment.tech/releases</url>
</repository>

<dependency>
  <groupId>tech.sbdevelopment</groupId>
  <artifactId>ShowControl</artifactId>
  <version>1.6</version>
  <scope>provided</scope>
</dependency>
```

### Controlling shows
You can find all the methods you need in the `SCAPI` class.

### Creating your own trigger
To learn how to implement your own triggers, check out [the default triggers](https://github.com/SBDPlugins/ShowControl/tree/master/src/main/java/tech/sbdevelopment/showcontrol/api/triggers/impl) for an example.

First, you need to create a new class inside your project. Use the following as a basis:
```java
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.util.List;

@NoArgsConstructor(force = true)
@TriggerIdentifier(value = "changeme", minArgs = 1, argDesc = "<arg0>", item = Material.GRASS_BLOCK)
public class MyTrigger extends Trigger {
  public MyTrigger(String[] data) throws InvalidArgumentException {
    super(data);
    //TODO Do something with the data, whatever you want.
    //You can throw InvalidArgumentException if an argument is invalid (not the type you except).
  }

  @Override
  public void trigger() {
    //TODO Called when the show reaches this trigger in one of its cues, execute the trigger here.
  }

  @Override
  public List<String> getArgumentTabComplete(Player player, int index, String arg) {
    //TODO Called when a user is adding this trigger as a cue, return a list of values for the provided arg. Starts on 0, which is the first argument of your trigger.
    return List.of();
  }

  @Override
  public void remove() {
    //TODO Called when the trigger cue is removed from a show, optional method!
  }
}
```

Then, you need to register your trigger(s) inside your plugin's Main class.
```java
@Override
public void onEnable() {
  //Insert this inside your onEnable method:
  SCAPI.index(YourMainClass.class, "the.path.to.the.package.containing.the.triggers.you.created");
}
```

Lastly, don't forget to include this plugin (`ShowControl`) inside your plugin.yml as a dependency.

## License
This project is licensed under the [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0).

The code in this project has been inspired by code licensed under the [OpenAudioMC License & Terms of Service](https://account.openaudiomc.net/terms). For more information, visit the [OpenAudioMc](https://github.com/Mindgamesnl/OpenAudioMc) project.

This project includes the [GuardianBeam](https://github.com/SkytAsul/GuardianBeam) library as a dependency, which is licensed under the [MIT License](https://opensource.org/licenses/MIT).

Please review the individual licenses for more details on usage and distribution.
