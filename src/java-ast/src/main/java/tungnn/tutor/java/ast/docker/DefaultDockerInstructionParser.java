package tungnn.tutor.java.ast.docker;

import com.github.jimschubert.docker.ast.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultDockerInstructionParser
    implements DockerInstructionParser<Map<String, Object>> {

  private static void put(Map<String, Object> map, String key, Object value) {
    switch (value) {
      case null -> {
        return;
      }
      case String s when s.isBlank() -> {
        return;
      }
      case java.util.Collection<?> c when c.isEmpty() -> {
        return;
      }
      default -> {}
    }

    map.put(key, value);
  }

  @Override
  public Map<String, Object> parse(FromInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "platform", instruction.getPlatform());
    put(result, "image", instruction.getImage());
    put(result, "digest", instruction.getDigest());
    put(result, "alias", instruction.getAlias());

    return result;
  }

  @Override
  public Map<String, Object> parse(RunInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "commands", instruction.getCommands());
    put(result, "heredoc", instruction.getHeredoc());
    put(result, "mounts", instruction.getMounts());
    put(result, "networkOption", instruction.getNetworkOption());
    put(result, "securityOption", instruction.getSecurityOption());

    return result;
  }

  @Override
  public Map<String, Object> parse(CopyInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "sources", instruction.getSources());
    put(result, "destination", instruction.getDestination());
    put(result, "from", instruction.getFrom());
    put(result, "chown", instruction.getChown());
    put(result, "chmod", instruction.getChmod());
    put(result, "exclude", instruction.getExclude());
    put(result, "link", instruction.getLink());
    put(result, "parents", instruction.getParents());

    return result;
  }

  @Override
  public Map<String, Object> parse(AddInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "sources", instruction.getSources());
    put(result, "destination", instruction.getDestination());
    put(result, "checksum", instruction.getChecksum());
    put(result, "chown", instruction.getChown());
    put(result, "chmod", instruction.getChmod());
    put(result, "keepGitDir", instruction.getKeepGitDir());
    put(result, "link", instruction.getLink());
    put(result, "exclude", instruction.getExclude());

    return result;
  }

  @Override
  public Map<String, Object> parse(CommandInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "form", instruction.getForm());
    put(result, "command", instruction.getCommand());

    return result;
  }

  @Override
  public Map<String, Object> parse(CmdInstruction instruction) {
    return parse((CommandInstruction) instruction);
  }

  @Override
  public Map<String, Object> parse(EntrypointInstruction instruction) {
    var result = parse((CommandInstruction) instruction);

    put(result, "entrypoint", instruction.getEntrypoint());

    return result;
  }

  @Override
  public Map<String, Object> parse(VolumeInstruction instruction) {
    var result = parse((CommandInstruction) instruction);

    put(result, "volume", instruction.getVolume());

    return result;
  }

  @Override
  public Map<String, Object> parse(EnvInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "variables", instruction.getVariables());

    return result;
  }

  @Override
  public Map<String, Object> parse(ExposeInstruction instruction) {
    var result = new LinkedHashMap<String, Object>();

    put(result, "ports", instruction.getPorts());

    return result;
  }
}
