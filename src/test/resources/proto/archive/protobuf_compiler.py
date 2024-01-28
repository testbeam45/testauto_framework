import os
import glob
from google.protobuf.compiler import plugin_pb2 as plugin
from google.protobuf import descriptor_pb2 as descriptor

def compile_protobufs(proto_directory, output_directory):
    # Ensure output directory exists
    os.makedirs(output_directory, exist_ok=True)

    # Find all .proto files in the proto directory
    proto_files = glob.glob(os.path.join(proto_directory, '*.proto'))

    for proto_file in proto_files:
        try:
            compile_protobuf(proto_file, output_directory)
        except Exception as e:
            print(f"Error compiling {proto_file}: {e}")

def compile_protobuf(proto_file, output_directory):
    # Example compilation logic
    print(f"Compiling {proto_file}...")
    # Your compilation logic here
    # Example: subprocess.run(['protoc', f'--python_out={output_directory}', proto_file], check=True)
    print(f"Compilation of {proto_file} completed.")

if __name__ == "__main__":
    # Example usage
    # Make the project directory relative
    project_directory = os.path.dirname(os.path.abspath(__file__))
    # Relative path to the directory containing .proto files
    proto_directory = os.path.join(project_directory, "src/test/resource/proto")
    # Relative path to the directory where generated files will be saved
    output_directory = os.path.join(project_directory, "src/test/java")
    compile_protobufs(proto_directory, output_directory)