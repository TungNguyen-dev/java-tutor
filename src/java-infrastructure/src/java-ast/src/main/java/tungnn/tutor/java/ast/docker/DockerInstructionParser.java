package tungnn.tutor.java.ast.docker;

import com.github.jimschubert.docker.ast.*;

public interface DockerInstructionParser<R> {

  default R parse(DockerInstruction instruction) {
    return switch (instruction) {
      case FromInstruction from -> parse(from);
      case RunInstruction run -> parse(run);
      case CopyInstruction copy -> parse(copy);
      case AddInstruction add -> parse(add);
      case CmdInstruction cmd -> parse(cmd);
      case EntrypointInstruction entrypoint -> parse(entrypoint);
      case VolumeInstruction volume -> parse(volume);
      case EnvInstruction env -> parse(env);
      case ExposeInstruction expose -> parse(expose);
      default ->
          throw new IllegalArgumentException(
              "Unsupported Docker instruction: " + instruction.getClass().getName());
    };
  }

  R parse(FromInstruction instruction);

  R parse(RunInstruction instruction);

  R parse(CopyInstruction instruction);

  R parse(AddInstruction instruction);

  R parse(CommandInstruction instruction);

  R parse(CmdInstruction instruction);

  R parse(EntrypointInstruction instruction);

  R parse(VolumeInstruction instruction);

  R parse(EnvInstruction instruction);

  R parse(ExposeInstruction instruction);
}
