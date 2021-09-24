#include "workers.h"


int main(int argc, char* argv[]) {
	Workers workers(4); 
	Workers eventLoop(1);
	workers.start_thread();
	eventLoop.start_thread();


	for (int i = 1; i <= 5; i++) {
		workers.post([i] {
			this_thread::sleep_for(1s); 
			});
	}

	workers.timeout([] {
		cout << "a" << endl;
		}, 6000);
	

	eventLoop.post([] {
		cout << "eventloop task" << endl; 

		}); 

	eventLoop.post([] {
		cout << "eventloop task" << endl;

		});


	eventLoop.stop(); 
	eventLoop.threads_close();

	workers.stop(); 
	workers.threads_close(); 

	cout << "done \n";

}